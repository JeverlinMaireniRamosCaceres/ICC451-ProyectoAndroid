const {setGlobalOptions} = require("firebase-functions");
const {onDocumentCreated} = require("firebase-functions/v2/firestore");
const logger = require("firebase-functions/logger");
const admin = require("firebase-admin");

admin.initializeApp();

setGlobalOptions({maxInstances: 10});

exports.notificarNuevoMensaje = onDocumentCreated(
    "chats/{chatId}/mensajes/{mensajeId}",
    async (event) => {
      const mensaje = event.data.data();
      const chatId = event.params.chatId;

      const remitenteUid = mensaje.remitenteuid;
      const destinatarioUid = chatId.split("_").find((uid) => uid !== remitenteUid);

      if (!destinatarioUid) {
        logger.warn("No se pudo determinar el destinatario", {chatId});
        return;
      }

      const db = admin.firestore();
      const destinatarioDoc = await db.collection("usuarios").doc(destinatarioUid).get();
      const remitenteDoc = await db.collection("usuarios").doc(remitenteUid).get();


      const token = destinatarioDoc.data() && destinatarioDoc.data().fcmToken;
      if (!token) {
        logger.warn("El destinatario no tiene fcmToken", {destinatarioUid});
        return;
      }

      const nombreRemitente = (remitenteDoc.data() && remitenteDoc.data().nombre) || "Nuevo mensaje";
      const hayTexto = mensaje.texto && mensaje.texto.trim() !== "";
      const cuerpo = hayTexto ? mensaje.texto : (mensaje.imagenUrl ? "Imagen" : "Nuevo mensaje");


      try {
        await admin.messaging().send({
          token: token,
          data: {
            titulo: nombreRemitente,
            cuerpo: cuerpo,
            remitenteUid: remitenteUid,
            remitenteNombre: nombreRemitente,
          },
          android: {
            priority: "high",
          },
        });
        logger.info("Notificación enviada", {destinatarioUid});
      } catch (error) {
        logger.error("Error al enviar la notificación", error);
      }
    },
);
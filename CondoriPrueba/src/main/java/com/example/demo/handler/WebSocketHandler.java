package com.example.demo.handler;



import org.springframework.web.socket.WebSocketSession;

import org.springframework.web.socket.handler.TextWebSocketHandler;

import org.springframework.web.socket.CloseStatus;

import org.springframework.web.socket.TextMessage;



import java.util.ArrayList;

import java.util.List;



public class WebSocketHandler extends TextWebSocketHandler {



    // Lista de sesiones activas

    private List<WebSocketSession> sessions = new ArrayList<>();

    private List<String> messages = new ArrayList<>();



    @Override

    public void handleTextMessage(WebSocketSession session, TextMessage message) {

        String textMessage = message.getPayload();

        messages.add(textMessage);



        // Si el mensaje contiene "salir", se envía una respuesta especial

        if (textMessage.contains("salir")) {

            String definition = "Chau usuario" ;

            for (WebSocketSession sess : sessions) {

                try {

                    sess.sendMessage(new TextMessage(definition));

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

        } else {

            // Enviar el mensaje a todos los clientes conectados

            for (WebSocketSession sess : sessions) {

                try {

                    sess.sendMessage(new TextMessage(textMessage));

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

        }

    }



    @Override

    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        // Agregar la nueva sesión a la lista de sesiones activas

        sessions.add(session);



        // Enviar todos los mensajes previos al cliente recién conectado

        for (String msg : messages) {

            session.sendMessage(new TextMessage(msg));

        }

    }



    @Override

    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        // Eliminar la sesión cerrada de la lista

        sessions.remove(session);

    }

}
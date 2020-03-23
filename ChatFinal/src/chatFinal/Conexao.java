package chatFinal;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static chatFinal.Servidor.conex;
import static chatFinal.Servidor.lista_usuarios;
import static chatFinal.Servidor.usuarios;

public class Conexao extends Thread {

    Socket cliente;
    String user = "";
    PrintStream saida = null;
    public boolean login;
    Login log = new Login();
    Servidor serv;

    Conexao(Socket conexao) {
        this.cliente = conexao;
    }

    // esse código é executando quando a thread é inicializada
    @Override
    public void run() {

        System.out.println("Nova conexão com o cliente " + cliente.getInetAddress().getHostAddress());

        Scanner entrada = null;
        try {
            entrada = new Scanner(cliente.getInputStream());

        } catch (IOException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            saida = new PrintStream(cliente.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scanner teclado = new Scanner(System.in);

        try {
            login = false;
            while (!login) {
                login = log.verificaLogin(cliente);
            }
            user = log.login[1];
            exibeUsuarios();

        } catch (IOException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (entrada.hasNextLine()) {

            String msg = entrada.nextLine();

            if (msg.equals("sair")) {
                if (usuarios.contains(user)) {
                    int i = usuarios.indexOf(user);
                    conex.remove(i);
                    usuarios.remove(i);
                    lista_usuarios.remove(i);
                    saida.close();
                    try {
                        cliente.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    exibeUsuarios();
                }
            }

            /*
             if (msg.equals("sair")) {
             for (int i = 0; i < usuarios.size(); i++) {
             if (conex.get(i).user.equals(usuarios.get(i))) {
             serv.conex.remove(i);
             serv.usuarios.remove(i);
             lista_usuarios.remove(i);
             System.out.println("Usuário "+ conex.get(i).user+" desconectado.");
             conex.get(i).saida.println("Usuário "+ conex.get(i).user+" desconectado.");
             exibeUsuarios();
             saida.close();
             try {
             cliente.close();
             } catch (IOException ex) {
             Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
             }
             }
             }
             }*/
            mensagem(msg);

            transmitir(msg);

        }
    }

    public void exibeUsuarios() {
        String lista = String.join(";", usuarios);
        for (int i = 0; i < conex.size(); i++) {
            if (conex.get(i).login) {
                conex.get(i).saida.println("lista_usuarios:" + lista);
            }
        }
    }

    public void mensagem(String msg) {

        String[] splitzinho = msg.split(":");

        if (splitzinho[0].equals("mensagem")) {

            if (splitzinho[1].equals("*")) {
                for (int i = 0; i < conex.size(); i++) {
                    System.out.println(user + " disse: " + msg);
                }
            } else {
                if (splitzinho[1].contains(";")) {
                    String[] nominhos = splitzinho[1].split(";");

                    for (int i = 0; i < conex.size(); i++) {
                        for (int j = 0; j < nominhos.length; j++) {
                            if (conex.get(i).user.equals(nominhos[j])) {
                                System.out.println(msg);
                            }
                        }

                    }

                } else {
                    String busca = splitzinho[1];
                    for (int i = 0; i < conex.size(); i++) {
                        if (conex.get(i).user.equals(busca)) {
                            System.out.println(msg);
                        }
                    }

                }

            }

        } else {
            System.out.println(msg);
        }
    }

    public void transmitir(String msg) {

        String[] splitzinho = msg.split(":");

        if (splitzinho[0].equals("mensagem")) {
            String mensagem = msg.substring(msg.lastIndexOf(":") + 1, msg.length());

            if (splitzinho[1].equals("*")) {
                for (int i = 0; i < conex.size(); i++) {
                    if (conex.get(i).login) 
                    conex.get(i).saida.println("transmitir:" + user + ":" + splitzinho[1] + ":" + mensagem);

                }
            } else {
                if (splitzinho[1].contains(";")) {
                    String[] nominhos = splitzinho[1].split(";");

                    for (int i = 0; i < conex.size(); i++) {
                        for (int j = 0; j < nominhos.length; j++) {
                            if (conex.get(i).user.equals(nominhos[j])) {
                                if (conex.get(i).login) 
                                conex.get(i).saida.println("transmitir:" + user + ":" + splitzinho[1] + ":" + mensagem);
                            }
                        }

                    }

                } else {
                    String busca = splitzinho[1];
                    for (int i = 0; i < conex.size(); i++) {
                        if (conex.get(i).user.equals(busca)) {
                            if (conex.get(i).login) 
                            conex.get(i).saida.println("transmitir:" + user + ":" + busca + ":" + mensagem);
                        }
                    }

                }

            }

        } else {
            System.out.println(user + " disse: " + msg);
            saida.println(msg);
        }
    }

}

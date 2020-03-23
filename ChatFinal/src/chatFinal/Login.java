package chatFinal;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import static chatFinal.Servidor.conex;
import static chatFinal.Servidor.lista_usuarios;
import static chatFinal.Servidor.usuarios;


/*
login:nome
    Mensagem enviada do cliente para o servidor para registrar o nome do usuário - DONE
    Respostas
        login:true
            O nome de usuário foi registrado com sucesso - DONE
        login:false
            O nome do usuário não pôde ser registrado pois é inválido ou já está em uso - DONE
        Exemplo:
            login:João - DONE
 */
public class Login {
    
    public String[] login;

    public boolean verificaLogin(Socket s) throws IOException {

        PrintStream saida = new PrintStream(s.getOutputStream());
        Scanner entrada = new Scanner(s.getInputStream());
        Conexao cnx = null;

        entrada.hasNextLine();
        String msg = entrada.nextLine();

        login = msg.split(":");
        if (login[0].equals("login")) {
            
            if (!usuarios.contains(login[1])) {
                usuarios.add(login[1]);
                lista_usuarios.add(login[1]+";");    
                System.out.println("login:true");
                saida.println("login:true");
                
                return true;
            } else {
                System.out.println("login:true");
                saida.println("login:false");
                return false;
            }
        } else {
            System.out.println("LOG:Server | Protocolo Incorreto");
            return false;
        }
        
    }
}

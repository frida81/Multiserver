package com.mycompany.multiserver;

import java.net.*; 
import java.io.*; 
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
class ServerThread extends Thread { 
  ServerSocket server      = null;
  Socket client            = null;
  String stringaRicevuta   = null;
  String stringaModificata = null;
  BufferedReader   inDalClient; 
  DataOutputStream outVersoClient;
  
  public ServerThread (Socket socket){ 
    this.client = socket; 
  } 
   
  public void run(){ 
  try{
    comunica();  
  }catch (Exception e){ 
    e.printStackTrace(System.out);  } 
  } 
  
  public void comunica ()throws Exception{ 
    inDalClient      = new BufferedReader(new InputStreamReader (client.getInputStream()));
    outVersoClient   = new DataOutputStream(client.getOutputStream());
    for (;;){
        
        //lettura della richiesta del client dei 5 numeri
      stringaRicevuta = inDalClient.readLine();
      if (stringaRicevuta == null || stringaRicevuta.equals("FINE")){         
        outVersoClient.writeBytes(stringaRicevuta+" (=>server in chiusura...)" + '\n');  
        System.out.println("Echo sul server in chiusura  :" + stringaRicevuta); 
        break;
      }
      else{
        //outVersoClient.writeBytes(stringaRicevuta+" (ricevuta e ritrasmessa)" + '\n'); 
        //TODO: generare 5 num casuali da 1 a 90 e inviarli al client
        int n= (int)(Math.random()*100 -10);
        outVersoClient.writeInt(n); 
        System.out.println("6 Echo sul server :" + stringaRicevuta); 
      }
    } 
    outVersoClient.close(); 
    inDalClient.close(); 
    System.out.println("9 Chiusura socket" + client); 
    client.close(); 
    MultiServer.numConnessioni--;
  } 
} 
  
public class MultiServer{ 
     //variabile statica per tenere traccia del numeri di connessioni attive
  static int numConnessioni=0;
  
  public void start(){ 
    try{
      ServerSocket serverSocket = new ServerSocket(6789); 
      for (;;) 
      { 
        System.out.println("1 Server in attesa ... "); 
        //verifica del num di connessioni e del tempo trascorso dall'ultima 
        //richiesta di connessione
       TimerTask task = new TimerTask() {
            public void run() {
                System.out.println("Task performed on: " + new Date() + "n" +
                  "Thread's name: " + Thread.currentThread().getName());
            }
        };
        Timer timer = new Timer("Timer");
    
        long delay = 1000L;
        timer.schedule(task, delay);
        

        
        Socket socket = serverSocket.accept(); 
        //TODO attesa di connessioni entro 60 secondi
        System.out.println("3 Server socket  " + socket); 
        ServerThread serverThread = new ServerThread(socket); 
        serverThread.start(); 
        MultiServer.numConnessioni++;
        System.out.println("connessioni totalizzate: "+MultiServer.numConnessioni);
      } 
    }
    catch (Exception e){
      System.out.println(e.getMessage());
      System.out.println("Errore durante l'istanza del server !");
      System.exit(1);
    }
  } 

  public static void main (String[] args){ 
     MultiServer tcpServer = new MultiServer(); 
     tcpServer.start(); 
   } 
}

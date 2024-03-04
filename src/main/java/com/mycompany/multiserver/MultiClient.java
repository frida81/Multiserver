package com.mycompany.multiserver;
import java.io.*;
import java.net.*;
public class MultiClient {
  String nomeServer ="localhost";                  // indirizzo server locale  
  int portaServer   = 6789;                        // porta x servizio data e ora
  Socket miosocket;                                
  BufferedReader tastiera;                         // buffer per l'input da tastiera
  String stringaUtente;                            // stringa inserita da utente
  String stringaRicevutaDalServer;                 // stringa ricevuta dal server
  DataOutputStream outVersoServer;                 // stream di output
  BufferedReader inDalServer;                      // stream di input 
  int[] numeri= new int[5];
  
  public void comunica() {
    for (;;)                                     // ciclo infinito: termina con FINE
    try{
      System.out.println("4 ... utente, inserisci la stringa da trasmettere al server:");
      stringaUtente = tastiera.readLine();
      //la spedisco al server 
      System.out.println("5 ... invio la stringa al server e attendo ...");
      outVersoServer.writeBytes( stringaUtente+'\n');
      //leggo la risposta dal server 
      //TODO leggo numeri
      //stringaRicevutaDalServer=inDalServer.readLine();
      
      numeri[0]=inDalServer.read();
      //System.out.println("7 ... risposta dal server "+'\n'+stringaRicevutaDalServer);
      System.out.println("7 ... numero risposta dal server: "+numeri[0]);
      if  (stringaUtente.equals("FINE")) { 
        System.out.println("8 CLIENT: termina elaborazione e chiude connessione" );
        miosocket.close();                             // chiudo la connessione
        break; 
      }
      //TODO: richiesta dei 5 numeri dal server e lettura dei suddetti da lui trasmessi
    } 
    catch (Exception e) 
    {
      System.out.println(e.getMessage());
      System.out.println("Errore durante la comunicazione col server!");
      System.exit(1);
    }
  }
  
  public Socket connetti(){
    System.out.println("2 CLIENT partito in esecuzione ...");
    try{
      // input da tastiera
      tastiera = new BufferedReader(new InputStreamReader(System.in));
      //  miosocket = new Socket(InetAddress.getLocalHost(), 6789);
      miosocket = new Socket(nomeServer,portaServer);
      // associo due oggetti al socket per effettuare la scrittura e la lettura 
      outVersoServer = new DataOutputStream(miosocket.getOutputStream());
      inDalServer    = new BufferedReader(new InputStreamReader (miosocket.getInputStream()));
    } 
    catch (UnknownHostException e){
      System.err.println("Host sconosciuto"); } 
    catch (Exception e){
      System.out.println(e.getMessage());
      System.out.println("Errore durante la connessione!");
      System.exit(1);
    }
    return miosocket;
  }

  public static void main(String args[]) {
    MultiClient cliente = new MultiClient();
    cliente.connetti();
    cliente.comunica();
  }   
}




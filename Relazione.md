Students Forum

Paolo Maffini, Simone Giuditta, Pier D’Agostino

Architettura
Il sito è stato progettato secondo il pattern Model-View-Controller (MVC) utilizzando la tecnologia JSP.

Le servlet si trovano nel package “servlet”. Esse svolgono la funzione di “controller” e contengono la logica dell’applicazione. Se una servlet deve restituire del codice HTML in output chiama la rispettiva “view”.

Le “view” sono delle pagine JSP posizionate nella cartella “web” del progetto. Ad ogni pagina JSP corrisponde una servlet che si occupa di gestire operazioni come la modifica e/o l’estrazione di dati dal database. Le pagine JSP prendono i dati elaborati dalle Servlet e li presentano in modo dinamico al client usando la libreria JSTL 1.2.2.

La parte dell’applicazione che gestisce il “model”, ovvero l’acceso al database, è contenuta nel package “db”. La classe DBManager implementa tutte le funzioni che richiedono accesso al database. Le altre classi nel package sono modelli che rappresentano le entità nel database come l’utente, il gruppo e il post.

//File

Piattaforme e framework
Abbiamo scelto di utilizzare GlassFish Server 4.0 come server web e Java 7 come linguaggio di programmazione.

Il database utilizzato è il database integrato in Netbeans, Derby. L’applicazione cerca di connettersi al database “students_forum” con username “students_forum” e password “password” all’indirizzo localhost e alla porta 1527.
Nella cartella del progetto è presente il file relations.sql che contiene le query per creare le tabelle e per popolare il forum con del contenuto come richiesto dalla consegna.

Come template per le view abbiamo usato la libreria JSTL 1.2.2 inclusa in NetBeans. Inoltre abbiamo fatto uso della libreria com.oreilly.servlet (cos.jar) per gestire le richieste multipart e javax.mail (javax.mail.jar) per l’invio delle email, utilizzando Gmail come server mail. Il file JAR delle librerie sono presenti nella cartella del progetto.

Per il lato client abbiamo fatto uso di due estensioni di JQuery: JQuery Mobile come framework per ottenere un’interfaccia multipiattaforma e DataTables per la visualizzazione avanzata di tabelle.
LIBS = lib/gson-2.10.1.jar

server:
	javac -cp $(LIBS):server -d . server/HOTELIERServerMain.java
	jar -cmvf server/META-INF/MANIFEST.MF HOTELIERserver.jar *.class $(LIBS)
	rm *.class
.PHONY: server
.SILENT: server

client:
	javac -cp $(LIBS):client -d . client/HOTELIERCustomerClientMain.java
	jar -cmvf client/META-INF/MANIFEST.MF HOTELIERclient.jar *.class $(LIBS)
	rm *.class
.PHONY: client
.SILENT: client

host: server
	java -jar HOTELIERserver.jar

run: client
	java -jar HOTELIERclient.jar
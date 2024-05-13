LIBS = lib/gson-2.10.1.jar

SERVER_DEPS = server/*.class server/database/*.class server/util/*.class server/core/*.class server/protocol/*.class

server:
	javac -cp $(LIBS):src -d . src/server/HOTELIERServerMain.java;
	jar -cmvf META-INF/SERVER.MF HOTELIERserver.jar $(SERVER_DEPS) $(LIBS);
	rm -rf server;
.PHONY: server
.SILENT: server

client:
	javac -cp $(LIBS):src -d . src/client/HOTELIERCustomerClientMain.java;
	jar -cmvf META-INF/CLIENT.MF HOTELIERclient.jar client/*.class $(LIBS);
	rm -rf client;
.PHONY: client
.SILENT: client

host: server
	java -jar HOTELIERserver.jar
.SILENT: server

run: client
	java -jar HOTELIERclient.jar
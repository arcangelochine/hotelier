server:
	javac -cp server -d . server/HOTELIERServerMain.java
	jar -cmvf server/META-INF/MANIFEST.MF HOTELIERserver.jar *.class
	rm *.class
.PHONY: server
.SILENT: server

client:
	javac -cp client -d . client/HOTELIERCustomerClientMain.java
	jar -cmvf client/META-INF/MANIFEST.MF HOTELIERclient.jar *.class
	rm *.class
.PHONY: client
.SILENT: client

host: server
	java -jar HOTELIERserver.jar

run: client
	java -jar HOTELIERclient.jar
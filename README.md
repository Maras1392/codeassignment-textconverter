Before Starting run: 
>mvn clean compile package

After .jar is created, you can run commands to start processing. There must be always two command parameters:
1) path to the file to be processed
2) type of conversion "csv" or "xml", other types are not suuported

Example:
>java -jar textconverter-0.0.1-SNAPSHOT.jar "C:\work\test_samples\small.in", "csv"

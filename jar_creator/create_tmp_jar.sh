jar cvf tmp.jar ../*.class ../pics
java -jar resource/jarsplice-0.25.jar
rm tmp.jar

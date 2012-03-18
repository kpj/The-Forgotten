output="The-Forgotten.jar"

echo "Main-Class: god" > mainClass
jar cmf mainClass $output *.class pics
rm mainClass

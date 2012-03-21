output="The-Forgotten.jar"

echo "Main-Class: god" > manifest
jar cmf manifest $output *.class pics data
rm manifest

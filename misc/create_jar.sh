# Execute from root directory

output="The-Forgotten.jar"
src="src"
bin="bin"

#python3 misc/compiler.py
javac -d bin src/*.java

cp $bin/* .
cp -r $src/pics $src/sounds $src/data $src/fonts .

echo "Main-Class: Startup" > manifest
jar cmf manifest $output *.class pics sounds data fonts
rm manifest

rm -r pics sounds data fonts
rm *.class

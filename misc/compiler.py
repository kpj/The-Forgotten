import os, pickle, hashlib

cur_dir = "/".join(os.path.realpath(__file__).split("/")[:-1])
save_file = cur_dir+"/compiler_saves"
comper = ".java"
to_compile = []

def get_hash_of_file(f):
	m = hashlib.md5()
	m.update(open(f, 'r').read().encode('utf-8'))
	return m.digest()

may_compile = {}
all = os.listdir(cur_dir+"/..")
for a in all:
	if comper in a:
		may_compile[a] = get_hash_of_file(a)

try:
	compiled = pickle.load(open(save_file, 'rb'))
except IOError:
	compiled = may_compile
	for c in compiled.keys():
		compiled[c] = "lol"

for e in all:
	if not comper in e:
		continue
	if may_compile[e] != compiled[e]:
		to_compile.append(e)

for tc in to_compile:
	print("Compiling %s..." % tc)
	os.system("javac %s" % tc)

pickle.dump(may_compile, open(save_file, 'wb'))

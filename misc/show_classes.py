import os, pickle, re

cur_dir = "/".join(os.path.realpath(__file__).split("/")[:-1])
save_file = cur_dir+"/classes_saves"
comper = ".java"
files = []

def short(lo):
	return str(lo.replace(comper, "").split("/")[-1])

def nicely_print(r):
	for w in r.keys():
		print(w+":", end=" ")
		for wo in r[w]:
			print(wo,end=", ")
		print("", end="\n")

def get_rel(f):
	out = []
	tmp = []
	code = open(f, 'rb').read()
	for fil in files:
		tmp.append(re.findall(bytes(short(fil).encode("utf-8")), code))
	for c in tmp:
		for ce in c:
			if not ce in out:
				out.append(ce.strip())
	return out

try:
	files = pickle.load(open(save_file, 'rb'))
except IOError:
	all = os.listdir(cur_dir+"/..")
	for a in all:
		if comper in a:
			files.append(cur_dir+"/../"+a)

relations = {}
for f in files:
	has = []
	rels = get_rel(f)
	for fi in files:
		fi = short(fi)
		for r in rels:
			r = str(r)
			if fi in r:
				if not has.__contains__(fi):
					has.append(fi)
	relations[short(f)] = has

nicely_print(relations)

#pickle.dump(files, open(save_file, 'wb'))


TEX_COMPILER = xelatex
FLAGS = -interaction=nonstopmode -halt-on-error

LITERATURE = bibtex

SOURCES := $(wildcard *.tex)
FILES := $(SOURCES:.tex=.pdf)

all:$(FILES)

%.pdf: %.tex
	$(TEX_COMPILER) $(FLAGS) $<
clean:
	rm -f *.out *aux *bbl *blg *log *toc *.ptb *.tod *.fls *.fdb_latexmk *.lof

realclean: clean
	rm -f *.pdf

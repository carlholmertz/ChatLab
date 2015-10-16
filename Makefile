
# make fil som bygger en .jar fil Client.jar frÃ¥n javafiler. Scriptet fÃ¶rvÃ¤ntar sig att klassen som main ligger i heter Client.java

# exempel lista pÃ¥ klasser som ingÃ¥r Ã¤ndrar ni den till era klasser borde ni inte behÃ¶va Ã¤ndra mer
SRC := ChatMain \
	ConnectToNameserver \
	PDUJoin \
	Checksum \
	ClientSender \
	OpCodes \
	PDU \
	PDUMessage \
	PDUNicks
OBJ := $(SRC:%=bin/%.class)

.PHONY:all clean distclean
all: Client.jar

Client.jar:$(OBJ)
		echo Main-Class: ChatMain > bin/main
		echo "" >> bin/main
		jar cmf bin/main Client.jar -C bin .
		rm bin/main

bin/%.class:%.java
	mkdir -p bin
		javac -Xlint:unchecked -d bin $<

clean:
		@-rm -r bin/*
		@-echo removing class files

distclean:clean
		@-rm Client.jar
		@-echo removing Client.jar
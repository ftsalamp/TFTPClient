# TFTPClient

This implementation of a TFTP Client follows RFC 1350.

This is not a full implementation. It only supports downloading a file.

To run this you should:

1) Go into the src directory
2) Compile all the Java files running `javac *`
3) Start the TFTP client running `java TFTPClient <IP ADDRESS> <FILENAME> <Operation Mode>`

The Operation Mode parameter has 2 acceptable values: **1** for receiving the file, and **2** for sending the file (currently not supported).

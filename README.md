## SitJar



_SitJar library is a collection of Java classes that turned out to be useful for developing prototype software._

It contains classes felt as missing in the java sdk as well as some convenience tools and helper classes. The whole library is continuously under development. Some part of the code is quite old (probably outdated). Some classes might even be under construction, so please do not simply rely on them without checking the code!

### Notable content:

* **sit.json.JSONParser**

    A very lightweight JSON parser - allowing to parse any JSON content. Compared to other JSON parsers this one is not requiring a pre-defined structure. 

* **sit.web.WebServer**

   A very easy to use Web Server. In particular adding ServiceEndpoints as specialized controller classes for handling http calls for certain endpoints can be done with only a few lines of code.

* **sit.sstl.ByteBuilder**

   Dynamic buffer for bytes. Useful for all kind of byte I/O (the byte equivalent of StringBuilder)


* **sit.sstl.HashMapSet.java**
 
   A HashMap containing Objects with a Key (ObjectWithKey). It guarantees that key of the object is used as the hash-key for the HashMaps entry.


* **sit.sstl.StrictSITEnumMap**

   Allows to use a EnumMap for binding Objects (StrictSITEnumContainer) to Enum types in a kind of Map. This allows for referencing rich objects by using a Enum type as selector providing compile-time checking for these objects.


* **Many more ...**

---

In case you find SitJar useful, or have any questions or issues, please feel free contact me. 

---

_(c) Copyright 2013 Simon Thiel_

This file is part of SitJar.

SitJar is free software: you can redistribute it and/or modify
it under the terms of the GNU LESSER GENERAL PUBLIC LICENSE as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

SitJar is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with SitJar. If not, see <http://www.gnu.org/licenses/lgpl.txt>.


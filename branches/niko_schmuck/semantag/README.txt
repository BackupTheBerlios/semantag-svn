================
Project semantag
================

A little code-experiment which was born after thinking how to make
use of Jelly for automatic topic map generation with the TM4J API.

Jelly ("Java & XML based scripting") is a relativley new tool from
Apache's Jakarta project which came to fame with the rise of Maven.
You may want to have a look at http://jakarta.apache.org/commons/jelly/
to figure out more details about Jelly.

Through its script-based paradigm, Jelly is enourmous flexible and allows
to access a wide range of different tag libraries. Those make the
processing inside a "topic map template script" very powerful, access
other data sources (ie. per SQL queries, XPath on documents).
Jelly makes use of the same type of expression language (EL) as it
is used in the JavaServer Pages Standard Tag Library (JSTL, see 
http://java.sun.com/products/jsp/jstl/) to evaluate expressions inside
jelly scripts.

-------------------------------------------------------------------------

Introducing access to the topic map tags in your jelly script:

<j:jelly xmlns:j="jelly:core" xmlns:x="jelly:xml"
         xmlns:tm="jelly:org.tm4j.jelly.TopicMapTagLibrary">
  ...
</j:jelly>


List of tags provided by semantag currently:

<tm:init baselocator="URI"/>

    Creates a new (in-memory) topic map object and places it in the
    jelly script environment context with name Dictionary.KEY_TOPICMAP

<tm:merge filename="FILE"/>

	Merges in a topic map (as specified by filename) to the current one.


<tm:addTopic [id]>

	Creates a new topic in the topic map and places a message in the
	warning log facility if a topic with the given id already exists.
	If no 'id' attribute was specified a IDGenerator object is
	invoked for retrieving an 'unique' ID.
	
Nested tags of <tm:addTopic>

  <tm:addInstanceOf type="TOPICID">
  
  <tm:addBasename [id] name="NAME">  
  
  <tm:addOccurrence data="DATA" [type]> 
  <tm:addOccurrence locator="URI" [type]>

It is also possible to place those tags outside the <tm:addTopic> element,
but then you have to specify a 'topic' attribute (holding the topic ID)
for referencing the corresponding topic.

As alternative way to specify the name the element <tm:addBasename>
allows that the element content is used as name data.
  
  
<tm:addAssociation [id]>

    Creates a new association in the topic map, this tag allows the 
    following child elements:

  <tm:addInstanceOf type="TOPICID">
  <tm:addMember player="TOPICID" role="TOPICID">    


<tm:save filename="FILE"/>

    Serializes the topic map as it is stored in the jelly script context
    to the given file.


To store a topic in the jelly script context as variable you may use: 

<tm:retrieveTopic var="VARNAME" 
                  id | subjectIndicator | sourceLocator = "">


-------------------------------------------------------------------------

Semantag uses basically:

- tm4j (0.9.6)
  http://tm4j.org/

- jelly (1.0-beta3)
  http://jakarta.apache.org/commons/jelly/

Both of these projects are based on other libraries, to run
the example you need the following jars in your CLASSPATH:

	antlr.jar
	commons-beanutils-1.6.jar
	commons-collections-3.0.jar
	commons-digester-1.5.jar
	commons-jelly-1.0-beta-3.jar
	commons-jexl-1.0-beta-1.jar
	commons-logging-1.0.3.jar
	dom4j-1.4.jar
	mango.jar
	resolver.jar
	tm4j-0.9.4.jar
	xerces-serialize.jar (extract from xercesImpl.jar)

For your convenience a small shell script (jelly.sh) is
also part of the prototype, this shell script needs one
argument, which is the name of the jelly script to execute.

Have fun,
Niko Schmuck

3-Jun-2004


=================
TODO:
=================

- Add validation to tags, which would be called when starting tag execution
  to verify all necessary attributes are correctly set and no ambiguity exists

- Support for more topic map concepts (for example improve scope support,
  <tm:addSubjectIndicator data="">)

- Improve object existance verification 

- many more things (please add your own problems / wishes)


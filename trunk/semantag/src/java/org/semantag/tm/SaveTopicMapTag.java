// $Id: SaveTopicMapTag.java,v 1.3 2004/12/09 21:19:58 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapProcessingException;
import org.tm4j.topicmap.utils.TopicMapWalker;
import org.tm4j.topicmap.utils.XTMWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Serializes a topic map to the specified file. 
 * 
 * @jelly
 *  name="saveTopicMap"
 * 
 * @author Niko Schmuck
 * @author cf
 */
public class SaveTopicMapTag extends BaseTMTag {
    /** The Log to which logging calls will be made. */
    private static final Log log = LogFactory.getLog(SaveTopicMapTag.class);

    private String filename;

    private TopicMap topicmap;

    public void setFilename(String filename) {
        this.filename = filename;
    }

    private TopicMap getTopicMap() throws JellyTagException {
        if (topicmap == null)
            topicmap = getTopicMapFromContext(null);
        return topicmap;
    }

    public void doTag(XMLOutput output) throws MissingAttributeException,
            JellyTagException {
        try {
            TopicMap tm = getTopicMap();
            File f = new File(filename);

            log.debug("Writing topic map to file " + f.getAbsolutePath());
            writeTopicMap(tm, new FileOutputStream(f));
        } catch (FileNotFoundException e) {
            throw new JellyTagException(e);
        }
    }

    private void writeTopicMap(TopicMap tm, OutputStream os) {
        TopicMapWalker walker = new TopicMapWalker();
        XTMWriter writer = new XTMWriter();
        OutputFormat of = new OutputFormat();

        of.setEncoding("UTF-8");
        of.setIndenting(true);
        of.setIndent(2);

        XMLSerializer serializer = new XMLSerializer(os, of);

        walker.setHandler(writer);
        writer.setContentHandler(serializer);

        try {
            walker.walk(tm);
        } catch (TopicMapProcessingException e) {
            throw new RuntimeException("Unable to write topic map: "
                    + e.toString());
        }
    }
    public TopicMap getTopicmap() {
        return topicmap;
    }
    /**
     * Sets the topicmap that shall be saved
     * @jelly
     *  required="no"
     * @param topicmap
     */
    public void setTopicmap(TopicMap topicmap) {
        this.topicmap = topicmap;
    }
}
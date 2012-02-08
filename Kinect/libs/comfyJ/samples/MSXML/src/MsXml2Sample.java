/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.automation.OleMessageLoop;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.Int32;

import java.lang.reflect.InvocationTargetException;

import msxml.IXMLHttpRequest;
import msxml.XMLHTTPRequest;
import msxml.IXMLDOMNodeList;
import msxml.IXMLDOMNode;
import msxml.impl.IXMLDOMDocumentImpl;


/**
 * This example shows how to use Microsoft MS XML Parser via ComfyJ library.
 * It downloads RSS feed from TeamDev site and then prints RSS news titles.
 */
public class MsXml2Sample
{
    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        OleMessageLoop.invokeAndWait(new Runnable()
        {
            public void run()
            {
                // Create XMLHTTPRequest COM object 
                IXMLHttpRequest httpRequest = XMLHTTPRequest.create(ClsCtx.INPROC_SERVER);
                // Open HTTP request
                httpRequest.open(
                        new BStr("GET"), // HTTP method
                        new BStr("http://support.teamdev.com/blogs/feeds/tags/company_news"), // HTTP url
                        new Variant(false), // async = false 
                        Variant.createUnspecifiedParameter(), // user name
                        Variant.createUnspecifiedParameter() // password
                );
                httpRequest.send(); // Send HTTP request and wait until it done

                // Uncomment following line to see the whole XML content
                // System.out.println("httpRequest returns " + httpRequest.getResponseText().getValue());

                // Enumerate RSS XML values and print them into System.out: 
                IXMLDOMDocumentImpl doc = new IXMLDOMDocumentImpl(httpRequest.getResponseXML());
                IXMLDOMNodeList ixmldomNodeList = doc.selectNodes(new BStr("rss/channel/item/title"));
                long nodesCount = ixmldomNodeList.getLengthProperty().getValue();
                for (int i = 0; i < nodesCount; ++i) {
                    IXMLDOMNode ixmldomNode = ixmldomNodeList.getItem(new Int32(i));
                    System.out.println(ixmldomNode.getText().getValue());                    
                }
            }
        });
    }
}

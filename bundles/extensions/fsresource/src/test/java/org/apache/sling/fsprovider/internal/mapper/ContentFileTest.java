/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.fsprovider.internal.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Map;

import org.apache.sling.api.resource.ValueMap;
import org.junit.Test;

public class ContentFileTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testRootContent() {
        File file = new File("src/test/resources/fs-test/folder2/content.json");
        
        ContentFile underTest = new ContentFile(file, null);
        assertEquals(file, underTest.getFile());
        assertNull(underTest.getSubPath());
        
        assertTrue(underTest.hasContent());

        Map<String,Object> content = (Map<String,Object>)underTest.getContent();
        assertEquals("app:Page", content.get("jcr:primaryType"));
        assertEquals("app:PageContent", ((Map<String,Object>)content.get("jcr:content")).get("jcr:primaryType"));

        ValueMap props = underTest.getValueMap();
        assertEquals("app:Page", props.get("jcr:primaryType"));
        assertNull(props.get("jcr:content"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testContentLevel1() {
        File file = new File("src/test/resources/fs-test/folder2/content.json");
        
        ContentFile underTest = new ContentFile(file, "jcr:content");
        assertEquals(file, underTest.getFile());
        assertEquals("jcr:content", underTest.getSubPath());
        
        assertTrue(underTest.hasContent());

        Map<String,Object> content = (Map<String,Object>)underTest.getContent();
        assertEquals("app:PageContent", content.get("jcr:primaryType"));

        ValueMap props = underTest.getValueMap();
        assertEquals("app:PageContent", props.get("jcr:primaryType"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testContentLevel5() {
        File file = new File("src/test/resources/fs-test/folder2/content.json");
        
        ContentFile underTest = new ContentFile(file, "jcr:content/par/image/file/jcr:content");
        assertEquals(file, underTest.getFile());
        assertEquals("jcr:content/par/image/file/jcr:content", underTest.getSubPath());
        
        assertTrue(underTest.hasContent());

        Map<String,Object> content = (Map<String,Object>)underTest.getContent();
        assertEquals("nt:resource", content.get("jcr:primaryType"));

        ValueMap props = underTest.getValueMap();
        assertEquals("nt:resource", props.get("jcr:primaryType"));
    }

    @Test
    public void testContentProperty() {
        File file = new File("src/test/resources/fs-test/folder2/content.json");
        
        ContentFile underTest = new ContentFile(file, "jcr:content/jcr:title");
        assertEquals(file, underTest.getFile());
        assertEquals("jcr:content/jcr:title", underTest.getSubPath());
        
        assertTrue(underTest.hasContent());

        assertEquals("English", underTest.getContent());

        assertTrue(underTest.getValueMap().isEmpty());
    }

    @Test
    public void testInvalidFile() {
        File file = new File("src/test/resources/fs-test/folder1/file1a.txt");
        ContentFile underTest = new ContentFile(file, null);
        assertFalse(underTest.hasContent());
    }

}
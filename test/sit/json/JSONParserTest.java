/*
* Copyright 2013 Simon Thiel
*
* This file is part of SitJar.
*
* SitJar is free software: you can redistribute it and/or modify
* it under the terms of the GNU LESSER GENERAL PUBLIC LICENSE as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* SitJar is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
* 
* You should have received a copy of the GNU Lesser General Public License
* along with SitJar. If not, see <http://www.gnu.org/licenses/lgpl.txt>.
*/

/*
 *  Description of JSONParserTest
 * 
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @date 02.05.2012
 */
package sit.json;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class JSONParserTest {

    private final static String TESTJSON = "{\n     \"firstName\": \"John\",\n     \"lastName\" : \"Smith\",\n     \"age\"      : 25,\n     \"address\"  :\n     {\n         \"streetAddress\": \"21 2nd Street\",\n         \"city\"         : \"New\\\\York\",\n         \"state\"        : \"NY\",\n         \"postalCode\"   : \"10021\"\n     },\n     \"phoneNumber\":\n     [\n         {\n           \"type\"  : \"home\",\n           \"number\": \"212 555-1234\"\n         },\n         {\n           \"type\"  : \"fax\",\n           \"number\": \"646 555-4567\"\n         }\n     ]\n }";
    private final static String REST_CALL_RESPONSE = "{\"response\":{\"meta\":{},\"data\":{\"startIndex\":0,\"itemsPerPage\":20,\"totalResults\":20,\"entry\":[{\"guid\":\"p_1\",\"name\":\"Achille\",\"imageUrl\":\"http://lorempixel.com/200/200/people/1\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_2\",\"name\":\"Adelheid\",\"imageUrl\":\"http://lorempixel.com/200/200/people/2\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_3\",\"name\":\"Xaverius\",\"imageUrl\":\"http://lorempixel.com/200/200/people/3\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_4\",\"name\":\"Yvette\",\"imageUrl\":\"http://lorempixel.com/200/200/people/4\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_5\",\"name\":\"Alkwin\",\"imageUrl\":\"http://lorempixel.com/200/200/people/5\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_1_1\",\"name\":\"Quintin\",\"imageUrl\":\"http://lorempixel.com/200/200/people/6\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_1_2\",\"name\":\"Juana\",\"imageUrl\":\"http://lorempixel.com/200/200/people/7\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_1_3\",\"name\":\"Seymour\",\"imageUrl\":\"http://lorempixel.com/200/200/people/8\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_1_4\",\"name\":\"Umberto\",\"imageUrl\":\"http://lorempixel.com/200/200/people/9\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_1_5\",\"name\":\"Sinja\",\"imageUrl\":\"http://lorempixel.com/200/200/people/10\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_2_1\",\"name\":\"Reinhard\",\"imageUrl\":\"http://lorempixel.com/200/200/people/1\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_2_2\",\"name\":\"Arne\",\"imageUrl\":\"http://lorempixel.com/200/200/people/2\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_2_3\",\"name\":\"Richard\",\"imageUrl\":\"http://lorempixel.com/200/200/people/3\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_2_4\",\"name\":\"Pascal\",\"imageUrl\":\"http://lorempixel.com/200/200/people/4\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_2_5\",\"name\":\"Kim\",\"imageUrl\":\"http://lorempixel.com/200/200/people/5\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_3_1\",\"name\":\"Baptiste\",\"imageUrl\":\"http://lorempixel.com/200/200/people/6\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_3_2\",\"name\":\"Boris\",\"imageUrl\":\"http://lorempixel.com/200/200/people/7\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_3_3\",\"name\":\"Indira\",\"imageUrl\":\"http://lorempixel.com/200/200/people/8\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_3_4\",\"name\":\"Trude\",\"imageUrl\":\"http://lorempixel.com/200/200/people/9\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_3_5\",\"name\":\"Vanessa\",\"imageUrl\":\"http://lorempixel.com/200/200/people/10\",\"type\":\"person\",\"items\":[]}]}}}";
    private final static String REST_CALL_RESPONSE2 = "{\"response\":{\"meta\":{},\"data\":{\"startIndex\":0,\"itemsPerPage\":3,\"totalResults\":3,\"entry\":[{\"guid\":\"g_1\",\"name\":\"Business\",\"imageUrl\":\"http://localhost:8080/images/group.png\",\"type\":\"group\",\"items\":[\"p_g_1_1\",\"p_g_1_2\",\"p_g_1_3\",\"p_g_1_4\",\"p_g_1_5\"]},{\"guid\":\"g_2\",\"name\":\"Friends\",\"imageUrl\":\"http://localhost:8080/images/group.png\",\"type\":\"group\",\"items\":[\"p_g_2_1\",\"p_g_2_2\",\"p_g_2_3\",\"p_g_2_4\",\"p_g_2_5\"]},{\"guid\":\"g_3\",\"name\":\"Family\",\"imageUrl\":\"http://localhost:8080/images/group.png\",\"type\":\"group\",\"items\":[\"p_g_3_1\",\"p_g_3_2\",\"p_g_3_3\",\"p_g_3_4\",\"p_g_3_5\"]}]}}}";
    private final static String REST_CALL_RESPONSE3 = "{\"response\":{\"meta\":{\"v\":\"0.1\",\"status\":\"OK\",\"code\":200,\"timeRef\":1320237974811},\"data\":{\"startIndex\":0,\"itemsPerPage\":3,\"totalResults\":3,\"entry\":[{\"guid\":\"urn:juan:juanPerson01/privacyPreference01\",\"type\":\"databox\",\"rdfs:label\":[\"DATABOX\",\"digital enterprise materials\"],\"name\":\"digital enterprise materials\",\"ppo:hasAccessSpace\":\"urn:juan:juanPerson01/privacyPreference01/accessSpace01\"},{\"guid\":\"urn:juan:juanPerson01/privacyPreference02\",\"type\":\"databox\",\"rdfs:label\":[\"DATABOX\",\"social media b2b\"],\"name\":\"social media b2b\",\"ppo:hasAccessSpace\":\"urn:juan:juanPerson01/privacyPreference02/accessSpace01\"},{\"guid\":\"urn:juan:juanPerson01/privacyPreference03\",\"type\":\"databox\",\"rdfs:label\":[\"DATABOX\",\"digitalHomeTechDocs\"],\"name\":\"digitalHomeTechDocs\",\"ppo:hasAccessSpace\":\"urn:juan:juanPerson01/privacyPreference03/accessSpace01\"}]}}}";
    private static JSONParser parser = new JSONParser();

    private static void testSpecialChars(String value) throws JSONParseException, JSONPathAccessException {
        JSONObject specialCharTestRoot = new JSONObject("foo");
        JSONObject specialCharTestValue = new JSONObject("bar");
        specialCharTestRoot.addChild(specialCharTestValue);
        specialCharTestValue.setValue(value, true);
        String setValue=  specialCharTestRoot.getChild("bar").toJson();
        JSONObject result = parser.parseJSON(specialCharTestRoot.toJson());
        String parsedValue = result.getChild("bar").getValue();
        String parsed2JValue = result.getChild("bar").toJson();
        System.out.println("(value/setValue/parsedValue/parsed2JValue):"+value+"<-/->"+setValue+"<-/->"+parsedValue+"<-/->"+parsed2JValue);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            testSpecialChars("aaaa\\bbbb");
            testSpecialChars("aaaa\"bbbb");
            testSpecialChars("aaaa\"\\\"bbbb");
            testSpecialChars("äöü");
            testSpecialChars("aaa\nbbb");
            testSpecialChars("aaa\fbbb");
            testSpecialChars("aaa\rbbb");
            testSpecialChars("aaa\tbbb");


//            System.out.println("json:\n" + TESTJSON);
//            System.out.println("-------------------------------");
//
//            JSONObject root = parser.getNextObject("root", TESTJSON, 0).object;
//            System.out.println("-----------------------------------------");
//            System.out.println(root.toJson());
//            System.out.println("-----------------------------------------");
//            root = parser.getNextObject("root", REST_CALL_RESPONSE2, 0).object;
//            System.out.println("-----------------------------------------");
//            System.out.println(root.toJson());
//            System.out.println("-----------------------------------------");
//            System.out.println(root.toString());
//            System.out.println("-----------------------------------------");
//
//            String[] guidPath = {"response", "data", "entry", "0", "guid"};
//            JSONObject guid = root.getChild(guidPath);
//            System.out.println("guid=" + guid.getValue());
//            guid.setValue("hello world", true);
//            guid = root.getChild(guidPath);
//            System.out.println("guid=" + guid.getValue());
//
//            root = parser.getNextObject("root", REST_CALL_RESPONSE3, 0).object;
//            System.out.println("-----------------------------------------");
//            System.out.println(root.toJson());
//            System.out.println("-----------------------------------------");
//            System.out.println(root.toString());
//            System.out.println("-----------------------------------------");




        } catch (JSONParseException ex) {

            //Logger.getLogger(JSONParser.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            for (StackTraceElement line : ex.getStackTrace()) {
                System.out.println(line.toString());
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            for (StackTraceElement line : ex.getStackTrace()) {
                System.out.println(line.toString());
            }
        }


    }
}

/*
 * $Id$
 *
 * Created on 30/06/2005
 *
 * Copyright (c) 2005, Ben Fortuna
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  o Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 *  o Neither the name of Ben Fortuna nor the names of any other contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.fortuna.ical4j.model;

import java.text.ParseException;
import java.util.Calendar;

import junit.framework.TestCase;

import net.fortuna.ical4j.util.TimeZones;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Ben Fortuna
 *
 */
public class DateTimeTest extends TestCase {

    private static Log log = LogFactory.getLog(DateTimeTest.class);
    
    private TimeZoneRegistry registry;

    /**
     * Default constructor.
     */
    public DateTimeTest() {
        registry = TimeZoneRegistryFactory.getInstance().createRegistry();
    }
    
    /*
     * Class under test for void DateTime(long)
     */
    public void testDateTimelong() {
        DateTime dt = new DateTime(0);
//        dt.setTimeZone(TimeZoneRegistryFactory.getInstance().createRegistry().getTimeZone(TimeZones.GMT_ID));
//        assertEquals("19700101T000000", dt.toString());
        
        dt.setUtc(true);
        assertEquals("19700101T000000Z", dt.toString());
    }

    /*
     * Class under test for void DateTime(Date)
     */
    public void testDateTimeDate() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.set(Calendar.YEAR, 1984);
        // months are zero-based..
        cal.set(Calendar.MONTH, 3);
        cal.set(Calendar.DAY_OF_MONTH, 17);
        cal.set(Calendar.HOUR_OF_DAY, 3);
        cal.set(Calendar.MINUTE, 15);
        cal.set(Calendar.SECOND, 34);
        assertEquals("19840417T031534", new DateTime(cal.getTime()).toString());
    }

    /*
     * Class under test for void DateTime(String)
     */
    public void testDateTimeString() throws Exception {
        try {
            new DateTime("20050630");
            fail("Should throw ParseException");
        }
        catch (ParseException pe) {
            log.info("Exception occurred: " + pe.getMessage());
        }
        assertEquals("20050630T093000", new DateTime("20050630T093000").toString());
        assertEquals("20050630T093000Z", new DateTime("20050630T093000Z").toString());
        
        assertEquals("20000402T020000", new DateTime("20000402T020000",
                registry.getTimeZone("America/Los_Angeles")).toString());
    }
    
    /**
     * Test equality of DateTime instances created using different constructors.
     * @throws ParseException
     */
    public void testDateTimeEquals() throws ParseException {
        DateTime date1 = new DateTime("20050101T093000");
    
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/UTC"));
        calendar.clear();
        calendar.set(2005, 0, 1, 9, 30, 00);
        calendar.set(Calendar.MILLISECOND, 1);
        DateTime date2 = new DateTime(calendar.getTime());
    
        assertEquals(date1.toString(), date2.toString());
        assertEquals(date1, date2);
    }

    /**
     * Test UTC date-times.
     */
    public void testUtc() throws ParseException {
        // ordinary date..
        DateTime date1 = new DateTime("20050101T093000");
        assertFalse(date1.isUtc());
        
        // UTC date..
        DateTime date2 = new DateTime(true);
        assertTrue(date2.isUtc());
        
        TimeZone utcTz = registry.getTimeZone(TimeZones.UTC_ID);
        utcTz.setID(TimeZones.UTC_ID);
        
        // UTC timezone, but not UTC..
        DateTime date3 = new DateTime("20050101T093000", utcTz);
//        date3.setUtc(false);
        assertFalse(date3.isUtc());
        
        DateTime date4 = new DateTime();
        date4.setUtc(true);
        assertTrue(date4.isUtc());
        date4.setUtc(false);
        assertFalse(date4.isUtc());

        DateTime date5 = new DateTime(false);
        date5.setTimeZone(utcTz);
        assertFalse(date5.isUtc());
    }
}

/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.thumbnails;

/**
 * Exception thrown when trying to get a thumbnail for an image that does not exist.
 * 
 * @version $Id: 43803c4e220a996896a90714d80f0909729827d5 $
 * @since 3.2-M3
 */
public class NoSuchImageException extends Exception
{

    /**
     * Generated serial UID. Change when the serialization form of this exception changes.
     */
    private static final long serialVersionUID = 9009950392582331776L;

    /**
     * Constructor of this exception.
     */
    public NoSuchImageException()
    {
        super();
    }

    /**
     * Constructor of this exception.
     * 
     * @param message the human-readable message associated with this exception instance.
     */
    public NoSuchImageException(String message)
    {
        super(message);
    }
}

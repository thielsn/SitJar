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
 *  @author Simon Thiel <simon.thiel at gmx.de>
 */
package sit.tools;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * ATTENTION - it is important for any derived class to
 *
 * 1) have a XMLRootElement set 
 * 
 * 2) have getters and setters defined for each
 * member field supported!!!
 *
 * @author simon
 */
@XmlRootElement(namespace = "http://morganasoft.de/")
public abstract class SettingsRoot {
}

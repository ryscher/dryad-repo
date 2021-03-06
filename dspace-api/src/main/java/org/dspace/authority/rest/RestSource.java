/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.authority.rest;

import org.dspace.authority.AuthoritySource;
import org.dspace.authority.AuthorityValue;

import java.util.List;

/**
 *
 * @author Antoine Snyers (antoine at atmire.com)
 * @author Kevin Van de Velde (kevin at atmire dot com)
 * @author Ben Bosman (ben at atmire dot com)
 * @author Mark Diggory (markd at atmire dot com)
 */
public abstract class RestSource implements AuthoritySource {

    protected RESTConnector restConnector;

    public RestSource(String url) {
        this.restConnector = new RESTConnector(url);
    }

    /**
     * TODO
     * com.atmire.org.dspace.authority.rest.RestSource#queryAuthorities -> add field, so the source can decide whether to query /users or something else.
     * -> implement subclasses
     * -> implement usages
     */
    public abstract List<AuthorityValue> queryAuthorities(String text, int max);

    public abstract AuthorityValue queryAuthorityID(String id);
}

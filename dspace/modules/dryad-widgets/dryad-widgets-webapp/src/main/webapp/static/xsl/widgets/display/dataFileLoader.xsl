<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:i18n="http://apache.org/cocoon/i18n/2.1"
    xmlns:bibo="http://purl.org/dryad/schema/dryad-bibo/v3.1"    
    xmlns:d1="http://ns.dataone.org/service/types/v1"
    xmlns:dcterms="http://purl.org/dc/terms/"
    xmlns:ddf="http://purl.org/dryad/schema/terms/v3.1"
    xmlns:dri="http://di.tamu.edu/DRI/1.0/"
    xmlns:dwc="http://rs.tdwg.org/dwc/terms/" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    
    xmlns:java="http://xml.apache.org/xalan/java"
    xmlns:sc="http://xml.apache.org/xalan/java/java.util.Scanner"
    xmlns:u="http://xml.apache.org/xalan/java/java.net.URL"
    exclude-result-prefixes="bibo d1 dcterms ddf dri dwc xsi java"
    version="1.0">

    <xsl:output method="html" indent="yes"/>
    <xsl:preserve-space elements="*"/>
    
    <xsl:param name="ddwcss"/>
    <xsl:param name="link1"/>
    <xsl:param name="link2"/>
    
    <xsl:param name="view-count"     select="/parts/dri/dri:document/dri:meta/dri:pageMeta/dri:metadata[@element='dryad'][@qualifier='pageviews']"/>
    <xsl:param name="download-count" select="/parts/dri/dri:document/dri:meta/dri:pageMeta/dri:metadata[@element='dryad'][@qualifier='downloads']"/>

    <xsl:variable name="publication-doi-url" select="/parts/object/ddf:DryadDataFile/ddf:isPartOf"/>
    <xsl:variable name="publication-doi-doi" select="substring-after($publication-doi-url,'http://dx.doi.org/')"/>
    
    <!-- -->
    <xsl:param name="bitstream-url"/>
    <xsl:variable name="format-id" select="/parts/meta-bitstream/d1:systemMetadata/formatId"/>

    <xsl:param name="download-link"/>
    
    <xsl:variable name="article-citation" select="/parts/dri/dri:document/dri:meta/dri:pageMeta/dri:metadata[@element='citation'][@qualifier='article']"/>
    <xsl:variable name="article-doi">
        <xsl:choose>
            <xsl:when test="starts-with(/parts/dri/dri:document/dri:meta/dri:pageMeta/dri:metadata[@element='identifier'][@qualifier='article'],'doi:')">
                <xsl:value-of select="substring-after(/parts/dri/dri:document/dri:meta/dri:pageMeta/dri:metadata[@element='identifier'][@qualifier='article'], 'doi')"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text></xsl:text>
            </xsl:otherwise>
        </xsl:choose>        
    </xsl:variable>
    <xsl:variable name="article-doi-url" select="concat('http://dx.doi.org/', $article-doi)"/>
    
    <xsl:variable name="title"       select="/parts/object/ddf:DryadDataFile/dcterms:title"/>
    <xsl:variable name="description" select="/parts/object/ddf:DryadDataFile/dcterms:description"/>
    
    <xsl:param name="datapackage-url"/>
    <xsl:param name="datapackage-img"/>
    
    <xsl:param name="request-origin"/>

    <xsl:template match="/">
        <html>
            <head>
                <xsl:call-template name="head-content"/>
            </head>
            <body id="dryad-ddw-body">
                <xsl:call-template name="body-content"/>
                <xsl:call-template name="make-meta"/>
                <xsl:call-template name="script-content"/>        
            </body>
        </html>
    </xsl:template>

    <xsl:template name="head-content">
        <link href="{$link1}"  rel="stylesheet" type="text/css"></link>
        <link href="{$link2}"  rel="stylesheet" type="text/css"></link>
        <link href="{$ddwcss}" rel="stylesheet" type="text/css" ></link>
    </xsl:template>

    <xsl:template name="body-content">
        <div class="dryad-ddw">
            <div class="dryad-ddw-header">
                <div class="dryad-ddw-banner">
                    <ul>
                        <li>
                            <a target="_blank" href="{$datapackage-url}">
                                <img src="{$datapackage-img}" alt="Data in Dryad"></img>
                            </a>
                        </li>
                        <li><b><xsl:value-of select="$publication-doi-doi"/></b></li>
                    </ul>
                </div>
                <div class="dryad-ddw-title">
                    <h1><xsl:value-of select="$title"/></h1>
                    <ul>
                        <li><b><xsl:value-of select="$view-count"/></b> views</li>
                        <li><b><xsl:value-of select="$download-count"/></b> downloads</li>
                    </ul>
                </div>
            </div>
            <div class="dryad-ddw-body">
                <div class="dryad-ddw-frame">
                    <xsl:call-template name="data-content"/>
                </div>
                <div class="dryad-ddw-control">
                    <ul>
                        <li><a class="dryad-ddw-zoom" title="Zoom"><i class="fa fa-expand"></i></a></li>
                        <li><a class="dryad-ddw-share" title="Share"><i class="fa fa-share-alt"></i></a></li>
                        <li><a class="dryad-ddw-download" title="Download" href="{$download-link}"><i class="fa fa-download"></i></a></li>
                        <li><a class="dryad-ddw-cite" title="Cite"><i class="fa fa-quote-left"></i></a></li>
                    </ul>
                </div>
            </div>
            <div class="dryad-ddw-footer">
                <div class="dryad-ddw-control">
                    <ul>
                        <li><a class="dryad-ddw-zoom" title="Zoom"><i class="fa fa-expand"></i></a></li>
                        <li><a class="dryad-ddw-share" title="Share"><i class="fa fa-share-alt"></i></a></li>
                        <li><a class="dryad-ddw-download" title="Download" href="{$download-link}"><i class="fa fa-download"></i></a></li>
                        <li><a class="dryad-ddw-cite" title="Cite"><i class="fa fa-quote-left"></i></a></li>
                    </ul>
                </div>
                <p><xsl:value-of select="$description"/></p>
            </div>
        </div>
    </xsl:template>

    <xsl:template name="make-meta">
        <div id="dryad-ddw-meta" class="dryad-ddw-hide" style="display:none !important;">
            <div id="dryad-ddw-citation" class="dryad-popup dryad-ddw dryad-ddw-citation">
                <div class="dryad-ddw-citation">
                    <img src="{$datapackage-img}" alt="Data in Dryad"></img>
                    <i18n:text>xmlui.DryadItemSummary.whenUsing</i18n:text>
                    <p class="shade">
                        <xsl:value-of select="$article-citation"/>
                        <a href="{$article-doi-url}">
                            <xsl:value-of select="$article-doi-url"/>
                        </a>
                    </p>
                    <i18n:text>xmlui.DryadItemSummary.pleaseCite</i18n:text>
                    <p class="shade">
                        Bradshaw WE, Emerson KJ, Holzapfel CM (2011) Data from: Genetic correlations and the evolution of photoperiodic time measurement within a local population of the pitcher-plant mosquito, Wyeomyia smithii. Dryad Digital Repository. <a href="http://dx.doi.org/10.5061/dryad.87ht85rs">http://dx.doi.org/10.5061/dryad.87ht85rs</a>                        
                    </p>
                    <p><i18n:text>xmlui.DryadItemSummary.downloadFormats</i18n:text></p>
                    <ul class="dryad-ddw-citation">
                        <li><a href="http://datadryad.org/resource/doi:10.5061/dryad.87ht85rs/citation/ris">RIS</a> 
                            <span><i18n:text>xmlui.DryadItemSummary.risCompatible</i18n:text></span>
                        </li>
                        <li><a href="http://datadryad.org/resource/doi:10.5061/dryad.87ht85rs/citation/bib">BibTex</a> 
                            <span><i18n:text>xmlui.DryadItemSummary.bibtexCompatible</i18n:text></span>
                        </li>
                    </ul>
                </div>
            </div>
            <div id="dryad-ddw-share" class="dryad-popup dryad-ddw dryad-ddw-share">
                <div class="dryad-ddw-share">
                    <img src="{$datapackage-img}" alt="Data in Dryad"></img>
                    <ul class="dryad-ddw-share">
                        <li><xsl:call-template name="reddit-item"/></li>
                        <li><xsl:call-template name="twitter-item"/></li>
                        <li><xsl:call-template name="facebook-item"/></li>
                        <li><xsl:call-template name="mendeley-item"/></li>            
                    </ul>
                </div>
            </div>
        </div>
    </xsl:template>
    
    <xsl:template name="reddit-item">
        <a href="http://reddit.com/submit" onclick="window.open('http://reddit.com/submit?url='+encodeURIComponent('http://dx.doi.org/doi:10.5061/dryad.87ht85rs')+'&amp;title=Data+from%3A+Robustness+of+compound+Dirichlet+priors+for+Bayesian+inference+of+branch+lengths.+','reddit','toolbar=no,width=550,height=550'); return false"><img alt="Reddit" src="http://reddit.com/static/spreddit7.gif" border="0px;"></img></a>
    </xsl:template>
    
    <xsl:template name="twitter-item">
        <iframe id="twitter-widget-0" scrolling="no" frameborder="0" allowtransparency="true" src="http://platform.twitter.com/widgets/tweet_button.1406859257.html#_=1407626732783&amp;count=none&amp;id=twitter-widget-0&amp;lang=en&amp;original_referer=http%3A%2F%2Fdatadryad.org%2Fresource%2Fdoi%3A10.5061%2Fdryad.87ht85rs%2F1&amp;size=m&amp;text=USYB-2011-142.SupplData%20from%3A%20Robustness%20of%20compound%20Dirichlet%20priors%20for%20Bayesian%20inference%20of%20branch%20lengths.%20-%20Dryad&amp;url=http%3A%2F%2Fdx.doi.org%2Fdoi%3A10.5061%2Fdryad.87ht85rs&amp;via=datadryad" class="twitter-share-button twitter-tweet-button twitter-share-button twitter-count-none" title="Twitter Tweet Button" data-twttr-rendered="true"></iframe>        
    </xsl:template>
    
    <xsl:template name="facebook-item">
        <iframe src="http://www.facebook.com/plugins/like.php?href=http%3A%2F%2Fdx.doi.org%2Fdoi%3A10.5061%2Fdryad.87ht85rs&amp;layout=button_count&amp;show_faces=false&amp;width=100&amp;action=like&amp;colorscheme=light" scrolling="no" frameborder="0" style="border:none; overflow:hidden; width:80px;height:21px;" allowtransparency="true"></iframe>
    </xsl:template>
    
    <xsl:template name="mendeley-item">
        <a href="http://www.mendeley.com/import/?url=http://datadryad.org/resource/doi:10.5061/dryad.87ht85rs">
            <img alt="Mendeley" src="http://www.mendeley.com/graphics/mendeley.png" border="0px;"></img>
        </a>        
    </xsl:template>
    
    <xsl:template name="script-content">
        <script type="text/javascript"><![CDATA[
(function(w,d) {
'use strict';]]>
var ddwcss = '<xsl:value-of select="$ddwcss"/>'
, jqlib    = '<xsl:value-of select="$jqlib"/>'
, lblib    = '<xsl:value-of select="$lblib"/>'
, wid      = '<xsl:value-of select="$wrapper-id"/>'
, bssrc    = '<xsl:value-of select="$frame-url"/>'
, height   = '<xsl:value-of select="$frame-height"/>'
, width    = '<xsl:value-of select="$frame-width"/>'<![CDATA[
, minJQ = ['1.7.2',1,7,2] // jQuery 1.7.2+ required for lightbox library
, pudel = 150  // lightbox close delay, ms.
, pucls = 'mfp-zoom-in' // css class for lightbox
, frcls = 'dryad-ddw'
, jQuery;
if (wid === undefined || wid === '') return;
bssrc = bssrc.concat('&origin=').concat(encodeURIComponent(document.location.origin));
if (w.jQuery === undefined || !testJQversion(w.jQuery.fn.jquery)) {
    load_js(jqlib, function(script) { 
        if (script.readyState) {        // IE
            script.readystatechange = function() {
                if (this.readyState === 'complete' || this.readyState == 'loaded') {
                    noConflictHandler();                
                }
            }
        } else {
            script.onload = noConflictHandler;
        }
    });
} else {
    jQuery = w.jQuery;
    dryadJQLoaded();
}
function testJQversion(jqv) {
    if (jqv === undefined) return false;
    var vs = jqv.match(/(\d+)\.(\d+)\.(\d+)/); // e.g., ["1.3.2", "1", "3", "2"]
    return    (parseInt(vs[1]) == minJQ[1] && parseInt(vs[2]) >= minJQ[2])  // jQuery 1.*
           || (parseInt(vs[1]) > minJQ[1])                                  // jQuery 2.*
}
function noConflictHandler() {
    jQuery = w.jQuery.noConflict();
    dryadJQLoaded();
}
function open_popup(content) {
    if (jQuery === undefined || !jQuery.hasOwnProperty('magnificPopup')) return;
    jQuery.magnificPopup.open({
        removalDelay: pudel,
        mainClass: pucls,
        items: {
            src: content,
            type: 'inline'
        }
    });
}
// download a URL using a hidden iframe element
function download_url(url) {
    var hiddenIFrameID = 'hiddenDownloader',
    iframe = document.getElementById(hiddenIFrameID);
    if (iframe === null) {
        iframe = document.createElement('iframe');
        iframe.id = hiddenIFrameID;
        iframe.style.display = 'none';
        document.body.appendChild(iframe);
    }
    iframe.src = url;
}
function handle_message(e) {
    // TODO: verify dryad source
    //if (e.origin !== d.location.protocol + '//' + d.location.host) return;
    if (e.data.hasOwnProperty('action')) {
        if (e.data.action === 'download') {
            if (!e.data.hasOwnProperty('data')) return;
            download_url(e.data.data);
        } else if (e.data.action === 'cite') {
            if (!e.data.hasOwnProperty('data')) return;
            open_popup(e.data.data);
        } else if (e.data.action === 'share') {
            if (!e.data.hasOwnProperty('data')) return;
            open_popup(e.data.data);      
        } else if (e.data.action === 'zoom') {
            if (!e.data.hasOwnProperty('data')) return;
            open_popup(e.data.data);
        }
    }
};
function load_js(url, cb) {
    var script = d.createElement('script');
    script.setAttribute('type', 'text/javascript');
    script.setAttribute('src', url);
    if (cb !== undefined) cb(script);
    (d.getElementsByTagName('script')[0]).insertBefore(script, null);
}
function load_css(url) {
    var link = d.createElement('link');
    link.setAttribute('rel', 'stylesheet');
    link.setAttribute('type', 'text/css');
    link.setAttribute('href', url);
    (d.getElementsByTagName('script')[0]).insertBefore(link, null);
}
function verify_load(url, pred, interval, count) {
    if (!(pred())) {
        var v;
        var f = function() {
            if (pred() || --count == 0) {
                clearInterval(v);
            } else {
                console.log('reattempting load of js');
                load_js(url);
            }
        };
        v = setInterval(f,interval);
    }
}
function dryadJQLoaded() {
    jQuery(d).ready(function($) {
        var wrapper = document.getElementById(wid);
        if (wrapper === null) return;
        var frame = document.createElement('iframe');
        frame.setAttribute('class', frcls);
        frame.setAttribute('src', bssrc);
        frame.setAttribute('width', width);
        frame.setAttribute('height', height);
        w.addEventListener('message', handle_message, false);
        load_css(ddwcss);
        load_js(lblib);
        if (!jQuery.hasOwnProperty('magnificPopup')) {
            load_js(lblib);
            verify_load(lblib, function() { return jQuery.hasOwnProperty('magnificPopup') }, 500, 10);
        }
        wrapper.appendChild(frame);
    });
}
zoomc.getElementsByClassName('dryad-ddw-frame')[0].classList.add('dryad-ddw-frame-full');
set_onclick(zooms, {"action" : "zoom", "data" : zoomc.outerHTML} );
})(window,document);

]]>
    </xsl:template>

    <xsl:template name="data-content">
        <iframe class="dryad-ddw-data" src="{$bitstream-url}"></iframe>
    </xsl:template>

</xsl:stylesheet>

/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (4.0.3).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package org.factomprotocol.identity.server.controller;

import did.DIDDocument;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-03-25T02:05:59.258+01:00[Europe/Berlin]")

@Validated
@Api(value = "UniversalResolver", description = "the UniversalResolver API")
public interface UniversalResolverApi {

    @ApiOperation(value = "Resolve a DID or other identifier (universal resolver).", nickname = "resolve", notes = "", response = Object.class, authorizations = {
        @Authorization(value = "ApiKeyAuth"),
        @Authorization(value = "BearerAuth"),
        @Authorization(value = "OAuth2", scopes = {
            @AuthorizationScope(scope = "read", description = "read dids"),
            @AuthorizationScope(scope = "write", description = "modify dids")
            })
    }, tags={ "UniversalResolver", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successfully resolved!", response = Object.class),
        @ApiResponse(code = 400, message = "invalid input!"),
        @ApiResponse(code = 500, message = "error!") })
    @RequestMapping(value = "/identifiers/{identifier}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<DIDDocument> _resolve(@ApiParam(value = "A DID or other identifier to be resolved.", required = true) @PathVariable("identifier") String identifier, @ApiParam(value = "") @RequestHeader(value = "Accept", required = false) Optional<String> accept);

}

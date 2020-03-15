package com.npf.cloud.swagger.api;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import io.swagger.models.Swagger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponents;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.PropertySourcedMapping;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.swagger.common.HostNameProvider;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.swagger.api
 * @ClassName: SwaggerController
 * @Author: ningpf
 * @Description: ${description}
 * @Date: 2020/3/15 16:01
 * @Version: 1.0
 */
@Slf4j
@Controller
@ApiIgnore
@RequestMapping(value = "${spring.mvc.prefix}")
public class SwaggerController {

    public static final String DEFAULT_URL = "/v2/api-docs";
    private static final String HAL_MEDIA_TYPE = "application/hal+json";
    private final String hostNameOverride;
    private final DocumentationCache documentationCache;
    private final ServiceModelToSwagger2Mapper mapper;
    private final JsonSerializer jsonSerializer;

    @Value("${spring.mvc.prefix}")
    private String mvcPrefix;

    @Autowired
    public SwaggerController(Environment environment, DocumentationCache documentationCache, ServiceModelToSwagger2Mapper mapper, JsonSerializer jsonSerializer) {
        this.hostNameOverride = environment.getProperty("springfox.documentation.swagger.v2.host", "DEFAULT");
        this.documentationCache = documentationCache;
        this.mapper = mapper;
        this.jsonSerializer = jsonSerializer;
    }

    @RequestMapping(
            value = {SwaggerController.DEFAULT_URL},
            method = {RequestMethod.GET},
            produces = {"application/json", SwaggerController.HAL_MEDIA_TYPE}
    )
    @PropertySourcedMapping(
            value = "${springfox.documentation.swagger.v2.path}",
            propertyKey = "springfox.documentation.swagger.v2.path"
    )
    @ResponseBody
    public ResponseEntity<Json> getDocumentation(@RequestParam(value = "group",required = false) String swaggerGroup, HttpServletRequest servletRequest) {
        String groupName = (String) Optional.fromNullable(swaggerGroup).or("default");
        Documentation documentation = this.documentationCache.documentationByGroup(groupName);
        if (documentation == null) {
            log.warn("Unable to find specification for group {}", groupName);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            Swagger swagger = this.mapper.mapDocumentation(documentation);
            UriComponents uriComponents = HostNameProvider.componentsFrom(servletRequest, swagger.getBasePath());
            swagger.basePath(Strings.isNullOrEmpty(uriComponents.getPath()) ? "/" : uriComponents.getPath().replace(mvcPrefix,""));
            if (Strings.isNullOrEmpty(swagger.getHost())) {
                swagger.host(this.hostName(uriComponents));
            }

            return new ResponseEntity(this.jsonSerializer.toJson(swagger), HttpStatus.OK);
        }
    }

    private String hostName(UriComponents uriComponents) {
        if ("DEFAULT".equals(this.hostNameOverride)) {
            String host = uriComponents.getHost();
            int port = uriComponents.getPort();
            return port > -1 ? String.format("%s:%d", host, port) : host;
        } else {
            return this.hostNameOverride;
        }
    }

    @RequestMapping(path = "/detectServer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map> detectServer(){
        Map<String,Object> result = new HashMap<>();

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(mvcPrefix);
        stringBuffer.append("服务");
        log.info(stringBuffer.toString());
        result.put("code","0");
        result.put("result",stringBuffer.toString());
        return new ResponseEntity(this.jsonSerializer.toJson(result), HttpStatus.OK);
    }
}

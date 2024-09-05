package ${package.Controller};

import org.springframework.web.bind.annotation.RequestMapping;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;
import io.github.yxsnake.pisces.web.core.framework.controller.BaseController;
/**
 *
 * @author pisces
 * @since ${date}
 */
@Slf4j
@Tag(name = "")
<#if restControllerStyle>
@RestController
@RequestMapping(value = "/api/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>", produces = MediaType.APPLICATION_JSON_VALUE)
<#else>
@Controller
@RequestMapping(value = "/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>", produces = MediaType.APPLICATION_JSON_VALUE)
</#if>
@AllArgsConstructor
<#if superControllerClass??>
@Validated
public class ${table.controllerName} extends ${superControllerClass}{
<#else>
public class ${table.controllerName} {
</#if>

}

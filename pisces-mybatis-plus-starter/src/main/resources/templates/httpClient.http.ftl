### ${entity}测试用例

<#if cfg.controllerRestApi>
  ### 创建${entity}
  POST {{host}}:{{port}}{{servlet-path}}/v1/api/${entityPath}/create
  Accept: {{application-json}}
  Content-Type: {{application-json}}

    ${insertJsonStr}

  > {%
  // 测试内容
  client.test("创建${entity}测试", function() {
  // 如果返回201则创建成功
  client.assert(response.status === 201, "Response status is not 201");
  });
  %}


  ### 修改${entity}
  POST {{host}}:{{port}}{{servlet-path}}/v1/api/${entityPath}/update/${httpPkValue}
  Accept: {{application-json}}
  Content-Type: {{application-json}}

    ${updateJsonStr}

  > {%
  client.test("修改${entity}测试", function() {
  client.assert(response.status === 200, "Response status is not 200");
  });
  %}


  ### ${entity}列表查询
  GET {{host}}:{{port}}{{servlet-path}}/v1/api/${entityPath}?pageSize=1&pageNum=1&searchCount=1&descs=id
  Accept: {{application-json}}
  Content-Type: {{application-json}}

  > {%
  client.test("${entity}列表查询测试", function() {
  client.assert(response.status === 200, "Response status is not 200");
  });
  %}


  ### 查询单个${entity}
  GET {{host}}:{{port}}{{servlet-path}}/v1/api/${entityPath}/${httpPkValue}
  Accept: {{application-json}}
  Content-Type: {{application-json}}

  > {%
  client.test("查询单个${entity}测试", function() {
  client.assert(response.status === 200 && response.body.data.id === '${httpPkValue}', "Test fail.");
  });
  %}


  ### 删除${entity}
  POST {{host}}:{{port}}{{servlet-path}}/v1/api/${entityPath}/delete/${httpPkValue}
  Accept: {{application-json}}
  Content-Type: {{application-json}}

  > {%
  client.test("删除${entity}测试", function() {
  client.assert(response.status === 204, "Response status is not 204");
  });
  %}

</#if>


###

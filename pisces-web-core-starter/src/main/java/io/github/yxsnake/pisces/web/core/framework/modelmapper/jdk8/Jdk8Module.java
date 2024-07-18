package io.github.yxsnake.pisces.web.core.framework.modelmapper.jdk8;

import org.modelmapper.ModelMapper;
import org.modelmapper.Module;

/**
 * Supports the JDK8 data types  with  ModelMapper
 *
 * @author
 */
public class Jdk8Module implements Module {

  @Override
  public void setupModule(ModelMapper modelMapper) {
    modelMapper.getConfiguration().getConverters().add(0, new FromOptionalConverter());
    modelMapper.getConfiguration().getConverters().add(0, new ToOptionalConverter());
  }
}


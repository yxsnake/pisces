package io.github.yxsnake.pisces.web.core.spring.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.executable.ExecutableValidator;
import jakarta.validation.metadata.BeanDescriptor;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 拓展SpringValidatorAdapter以便于校验List<T>类型数据 {@link org.springframework.validation.beanvalidation.SpringValidatorAdapter
 */
@SuppressWarnings("unchecked")
public class ValidatorCollectionImpl implements Validator {

  private final Validator targetValidator = Validation.buildDefaultValidatorFactory().getValidator();
  @SuppressWarnings("unchecked")
  @Override
  public <T> Set<ConstraintViolation<T>> validate(T t, Class<?>... groups) {
    Assert.state(this.targetValidator != null, "No target Validator set");
    if (t instanceof Collection) {
      Set<ConstraintViolation<T>> constraintViolations = new HashSet<>();
      Collection collection = (Collection) t;
      for (Object o : collection) {
        constraintViolations.addAll(this.targetValidator.validate((T) o, groups));
      }
      return constraintViolations;
    }
    return this.targetValidator.validate(t, groups);
  }

  @Override
  public <T> Set<ConstraintViolation<T>> validateProperty(T t, String propertyName, Class<?>... groups) {
    Assert.state(this.targetValidator != null, "No target Validator set");
    return this.targetValidator.validateProperty(t, propertyName, groups);
  }

  @Override
  public <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value, Class<?>... groups) {
    Assert.state(this.targetValidator != null, "No target Validator set");
    return this.targetValidator.validateValue(beanType, propertyName, value, groups);
  }

  @Override
  public BeanDescriptor getConstraintsForClass(Class<?> clazz) {
    Assert.state(this.targetValidator != null, "No target Validator set");
    return this.targetValidator.getConstraintsForClass(clazz);
  }

  @Override
  public <T> T unwrap(Class<T> type) {
    Assert.state(this.targetValidator != null, "No target Validator set");
    try {
      return (type != null ? this.targetValidator.unwrap(type) : (T) this.targetValidator);
    } catch (ValidationException ex) {
      // ignore if just being asked for plain Validator
      if (Validator.class == type) {
        return (T) this.targetValidator;
      }
      throw ex;
    }
  }

  @Override
  public ExecutableValidator forExecutables() {
    return targetValidator.forExecutables();
  }
}

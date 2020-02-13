/**
 * Autogenerated by Thrift Compiler (0.9.0-dev)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package mto.passport.profilev2.db.thrift;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum TReturnCode implements org.apache.thrift.TEnum {
  OK(0),
  KEY_NOT_FOUND(1),
  KEY_EXISTED(2),
  ERROR(3);

  private final int value;

  private TReturnCode(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static TReturnCode findByValue(int value) { 
    switch (value) {
      case 0:
        return OK;
      case 1:
        return KEY_NOT_FOUND;
      case 2:
        return KEY_EXISTED;
      case 3:
        return ERROR;
      default:
        return null;
    }
  }
}

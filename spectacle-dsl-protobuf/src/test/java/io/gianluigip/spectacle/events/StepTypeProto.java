// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: spectacle/SpecificationStep.proto

package io.gianluigip.spectacle.events;

/**
 * Protobuf enum {@code spectacle.events.StepTypeProto}
 */
public enum StepTypeProto
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>GIVEN = 0;</code>
   */
  GIVEN(0),
  /**
   * <code>WHENEVER = 1;</code>
   */
  WHENEVER(1),
  /**
   * <code>THEN = 2;</code>
   */
  THEN(2),
  UNRECOGNIZED(-1),
  ;

  /**
   * <code>GIVEN = 0;</code>
   */
  public static final int GIVEN_VALUE = 0;
  /**
   * <code>WHENEVER = 1;</code>
   */
  public static final int WHENEVER_VALUE = 1;
  /**
   * <code>THEN = 2;</code>
   */
  public static final int THEN_VALUE = 2;


  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @Deprecated
  public static StepTypeProto valueOf(int value) {
    return forNumber(value);
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   */
  public static StepTypeProto forNumber(int value) {
    switch (value) {
      case 0: return GIVEN;
      case 1: return WHENEVER;
      case 2: return THEN;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<StepTypeProto>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      StepTypeProto> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<StepTypeProto>() {
          public StepTypeProto findValueByNumber(int number) {
            return StepTypeProto.forNumber(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    if (this == UNRECOGNIZED) {
      throw new IllegalStateException(
          "Can't get the descriptor of an unrecognized enum value.");
    }
    return getDescriptor().getValues().get(ordinal());
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return SpecificationStep.getDescriptor().getEnumTypes().get(0);
  }

  private static final StepTypeProto[] VALUES = values();

  public static StepTypeProto valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    if (desc.getIndex() == -1) {
      return UNRECOGNIZED;
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private StepTypeProto(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:spectacle.events.StepTypeProto)
}

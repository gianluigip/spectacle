// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: spectacle/SpecificationStep.proto

package io.gianluigip.spectacle.events;

public interface SpecificationStepProtoOrBuilder extends
    // @@protoc_insertion_point(interface_extends:spectacle.events.SpecificationStepProto)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.spectacle.events.StepTypeProto type = 1;</code>
   * @return The enum numeric value on the wire for type.
   */
  int getTypeValue();
  /**
   * <code>.spectacle.events.StepTypeProto type = 1;</code>
   * @return The type.
   */
  StepTypeProto getType();

  /**
   * <code>string description = 2;</code>
   * @return The description.
   */
  String getDescription();
  /**
   * <code>string description = 2;</code>
   * @return The bytes for description.
   */
  com.google.protobuf.ByteString
      getDescriptionBytes();

  /**
   * <code>int32 index = 3;</code>
   * @return The index.
   */
  int getIndex();
}
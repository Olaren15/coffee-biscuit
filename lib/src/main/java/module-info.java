import org.jspecify.annotations.NullMarked;

@NullMarked
module dev.olaren.coffee_biscuit {
  requires org.jspecify;
  requires org.apache.commons.collections4;

  exports dev.olaren.coffee_biscuit;
  exports dev.olaren.coffee_biscuit.sqpack;
  exports dev.olaren.coffee_biscuit.sqpack.data_types;
}

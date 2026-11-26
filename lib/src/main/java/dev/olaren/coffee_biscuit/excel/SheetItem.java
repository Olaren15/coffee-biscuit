package dev.olaren.coffee_biscuit.excel;

public class SheetItem {
  protected final Page<? extends SheetItem> page;
  protected final long id;
  protected final long fixedDataOffset;
  protected final long variableDataOffset;

  public SheetItem(
      Page<? extends SheetItem> page, long id, long fixedDataOffset, long variableDataOffset) {
    this.page = page;
    this.id = id;
    this.fixedDataOffset = fixedDataOffset;
    this.variableDataOffset = variableDataOffset;
  }

  public long getId() {
    return id;
  }
}

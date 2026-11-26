package dev.olaren.coffee_biscuit.excel.sheet;

import dev.olaren.coffee_biscuit.excel.Page;
import dev.olaren.coffee_biscuit.excel.SheetItem;
import dev.olaren.coffee_biscuit.excel.reflection.SheetName;

@SheetName("Orchestrion")
public class Orchestrion extends SheetItem {
  private static final long NAME_OFFSET = 0;
  private static final long DESCRIPTION_OFFSET = 4;

  public Orchestrion(Page<Orchestrion> page, long id, long offset, long variableDataOffset) {
    super(page, id, offset, variableDataOffset);
  }

  public String getName() {
    return page.readString(fixedDataOffset + NAME_OFFSET, variableDataOffset);
  }

  public String getDescription() {
    return page.readString(fixedDataOffset + DESCRIPTION_OFFSET, variableDataOffset);
  }
}

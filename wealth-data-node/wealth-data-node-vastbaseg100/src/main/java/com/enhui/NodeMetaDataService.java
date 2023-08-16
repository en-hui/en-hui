package com.enhui;

import static com.enhui.model.AllTypeTableColumn.ALL_COLUMN_TYPE;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class NodeMetaDataService {

  @Test
  public void createAllTypeTable() {
    AtomicInteger i = new AtomicInteger(1);
    String columnSql =
        ALL_COLUMN_TYPE.stream()
            .map(
                colu ->
                    String.format("%s %s", colu.getName() + i.getAndIncrement(), colu.getType()))
            .collect(Collectors.joining(",\n"));

    String createSql = String.format("CREATE TABLE heh_all_type1(%s);", columnSql);
    System.out.println(createSql);
  }
}

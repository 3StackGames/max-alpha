package com.three_stack.maximum_alpha.backend.game.utilities;

import java.util.List;
import java.util.Map;

import com.three_stack.maximum_alpha.backend.game.State;

public class ValueExpression {

  private Expression expression;
  private char variable = 'a';
  private boolean constant;
  private int amount;

  public ValueExpression (Object value) {
    if (value.getClass() == int.class) {
      amount = (int) value;
      constant = true;
    } 
    else {
      expression = new Expression(value);
      constant = false;
    }
  }

  public int eval(State state) {
    if (!constant) {  
      List<Map<String, Object>> variables = (List<Map<String, Object>>) value.get("variables"); //TODO fix
  
      for (Map<String, Object> variable : variables) {
        String type = (String) variable.get("type");
    
        switch (type) {
          case "attribute": 
            String attributeName = (String) variable.get("attribute");
            Attribute attribute = state.getAttributes().get(attributeName);         
            List<String> filterNames = (List<String>) variable.get("filters");
            List<Filter> filters = filterNames.stream().
                map(filter -> Filter.valueOf(filter)).collect(Collectors.toList());
            
            expression.with(variable++, attribute.process(filters));
            break;
          default:
            break;
        }
      }
  
      variable = 'a';
      return expression.eval();
    }
    
    return amount;
  }
}
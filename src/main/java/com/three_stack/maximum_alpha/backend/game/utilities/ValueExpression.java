package com.three_stack.maximum_alpha.backend.game.utilities;

import java.util.List;
import java.util.Map;

import com.three_stack.maximum_alpha.backend.game.State;

//TODO: move to better location
//TODO: something else - forgot what
public class ValueExpression {

	private Object value;

	public ValueExpression (Object value) {
		this.value = value;
	}

	public int eval(State state) {
		if (value.getClass() == int.class) {
			return (int) value;
		}

		Map<String, Object> expressionValue = (Map<String, Object>) value;
		Expression expression = new Expression((String) expressionValue.get("expression"));
		char variableName = 'a';
		List<Map<String, Object>> variables = (List<Map<String, Object>>) expressionValue.get("variables");

		for (Map<String, Object> variable : variables) {
			String type = (String) variable.get("type");

			switch (type) {
				case "attribute": 
					String attributeName = (String) variable.get("attribute");
					Attribute attribute = state.getAttributes().get(attributeName);         
					List<String> filterNames = (List<String>) variable.get("filters");
					List<Filter> filters = filterNames.stream().
						map(filter -> Filter.valueOf(filter)).collect(Collectors.toList());
					
					expression.with(variableName++, attribute.process(filters));
					break;
				default:
					break;
			}
		}

		return expression.eval();
	}
}
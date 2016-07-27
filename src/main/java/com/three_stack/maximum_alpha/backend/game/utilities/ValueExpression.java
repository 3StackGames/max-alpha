package com.three_stack.maximum_alpha.backend.game.utilities;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.attributes.Attribute;
import com.three_stack.maximum_alpha.backend.game.attributes.Attribute.Filter;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.udojava.evalex.Expression;

//TODO: move to better location
public class ValueExpression {

	private Object value;

	public ValueExpression (Object value) {
		this.value = value;
	}

	public int eval(State state, Card source) {
		if (value instanceof Integer) {
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
					Attribute attribute = state.getAttribute(attributeName);         
					List<String> filterNames = (List<String>) variable.get("filters");
					List<Filter> filters = filterNames.stream().
						map(filter -> Filter.valueOf(filter)).collect(Collectors.toList());
					
					expression.with(Character.toString(variableName++), new BigDecimal(attribute.getValue(filters, source)));
					break;
				default:
					break;
			}
		}

		return expression.eval().intValueExact();
	}
}
/*
 * 		Copyright 2017 Emmanuel CHEBBI
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.kazejiyu.generic.datatable.annotation;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

@SupportedAnnotationTypes("fr.kazejiyu.generic.datatable.annotation.Internal")
public class InternalProcessor extends AbstractProcessor {
	
	private ProcessingEnvironment env;
	
	@Override
	public synchronized void init(ProcessingEnvironment pe) {
		this.env = pe;
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if( ! roundEnv.processingOver() ) {
			for( TypeElement annotation : annotations ) {
				final Set <? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
				
				for( Element element : elements ) {
					String message = ((Internal) annotation).message();
					message = "Access restriction : this element is API-limited"
							+ (message.isEmpty() ? "" : " (" + message + ")");
				
					this.env.getMessager().printMessage(Kind.ERROR, message, element);
				}
			}
		}
		return false;
	}

}

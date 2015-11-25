/*******************************************************************************
 * Copyright 2013 Raphael Jolivet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 ******************************************************************************/
package java2typescript.jackson.module.grammar;

import static java.lang.String.format;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java2typescript.jackson.module.grammar.base.AbstractNamedType;
import java2typescript.jackson.module.grammar.base.AbstractType;

public class Module {

    private String name;

    private final Map<String, AbstractNamedType> namedTypes = new HashMap<String, AbstractNamedType>();

    private final Map<String, AbstractType> vars = new HashMap<String, AbstractType>();

    public Module() {

    }

    public Module(
                final String name) {

        this.name = name;
    }

    public Map<String, AbstractNamedType> getNamedTypes() {

        return namedTypes;
    }

    public Map<String, AbstractType> getVars() {

        return vars;
    }

    public void setName(final String name) {

        this.name = name;
    }

    public String getName() {

        return name;
    }

    public void write(final Writer writer) throws IOException {

        writer.write(format("declare module \"%s\" {\n\n", name));

        for (final AbstractNamedType type : namedTypes.values()) {
            writer.write("export ");
            type.writeDef(writer);
            writer.write("\n\n");
        }

        for (final Entry<String, AbstractType> entry : vars.entrySet()) {
            writer.write("export var " + entry.getKey() + ": ");
            entry.getValue().write(writer);
            writer.write(";\n");
        }

        writer.write("}\n");
        writer.flush();
    }

}

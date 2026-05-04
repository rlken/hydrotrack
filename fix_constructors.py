import os
import re

directories = [
    'src/main/java/com/hydrotrack/service',
    'src/main/java/com/hydrotrack/controller'
]

for d in directories:
    for filename in os.listdir(d):
        if filename.endswith('.java'):
            filepath = os.path.join(d, filename)
            with open(filepath, 'r') as f:
                content = f.read()

            # Find if it has @RequiredArgsConstructor
            if '@RequiredArgsConstructor' in content:
                # Remove the annotation
                content = content.replace('@RequiredArgsConstructor\n', '')
                content = content.replace('import lombok.RequiredArgsConstructor;\n', '')
                
                # Find the class name
                class_match = re.search(r'public class (\w+)', content)
                if not class_match:
                    continue
                class_name = class_match.group(1)
                
                # Find all 'private final Type name;' declarations
                fields = re.findall(r'private final (\w+(?:<\w+>)?)\s+(\w+);', content)
                
                if fields:
                    # Construct the constructor
                    params = ', '.join([f'{t} {n}' for t, n in fields])
                    assignments = '\n'.join([f'        this.{n} = {n};' for t, n in fields])
                    
                    constructor = f'\n    public {class_name}({params}) {{\n{assignments}\n    }}\n'
                    
                    # Insert constructor after the last private final field
                    # Find the last private final field position
                    last_field = f'private final {fields[-1][0]} {fields[-1][1]};'
                    pos = content.rfind(last_field) + len(last_field)
                    
                    new_content = content[:pos] + '\n' + constructor + content[pos:]
                    
                    with open(filepath, 'w') as f:
                        f.write(new_content)
                    print(f"Fixed {filepath}")


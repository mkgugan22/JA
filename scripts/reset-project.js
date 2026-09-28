#!/usr/bin/env node

const fs = require('fs');
const path = require('path');

const root = process.cwd();
const appDir = path.join(root, 'src', 'app');
const exampleDir = path.join(root, 'src', 'app-example');

if (!fs.existsSync(appDir)) {
  console.error('src/app does not exist');
  process.exit(1);
}

fs.mkdirSync(exampleDir, { recursive: true });

for (const file of fs.readdirSync(appDir)) {
  const src = path.join(appDir, file);
  const dest = path.join(exampleDir, file);
  fs.renameSync(src, dest);
}

fs.writeFileSync(path.join(appDir, 'index.tsx'), '// Start your app here.\n');
fs.writeFileSync(path.join(appDir, 'explore.tsx'), '// Add another route here.\n');
console.log('Moved starter routes to src/app-example.');

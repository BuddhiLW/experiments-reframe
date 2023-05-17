# Playground for MUI, re-frame and tailwind.

Studies on re-frame, MUI and tailwind.

Here you will find many examples where we mix MUI and tailwind. How to reference 
MUI components and interact with them etc.

## Production

Run a production build in one command:

    yarn build

This generates all minified and optimized files in `public` ready to get deployed.

## Development

Create a copy of this project. Then, run the following commands:

```bash
# install dependencies
yarn

# start development server and tailwindcss
yarn dev
```

Open your browser at http://localhost:8000/. You now have a REPL running,
hot-reloading of CSS and your ClojureScript files.

`yarn dev` runs both, shadow-cljs and tailwindcss in a watch-mode so that you
have an interactive development experience. You can, of course, start those jobs
individually or connect to your REPL from your editor.

## License

This project is [MIT licensed](./LICENSE).

<!-- postgresql://username:password@host:port/dtabase_name -->
<!-- postgresql://postgres:postgres@localhost:8080/playground -->
<!-- psql postgresql://postgres:postgres@localhost:8080/postgres -->

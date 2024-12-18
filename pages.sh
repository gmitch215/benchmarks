git config --local user.email "action@github.com"
git config --local user.name "GitHub Action"
git fetch origin gh-pages

if [ ! -d "site" ]; then
  mkdir site
fi;

cp -Rfv build/site/* ./site/

git switch -f gh-pages

for dir in ./*
do
  if [ "$dir" == "./site" ]; then
    continue
  fi

  rm -rf "$dir"
done

cp -Rfv ./site/* ./
rm -rf ./site

echo "benchmarks.gmitch215.xyz" > CNAME

git add .
git commit -m "Update Benchmarks Site"
git push -f origin gh-pages

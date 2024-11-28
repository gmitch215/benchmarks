git config --global user.name 'github-actions[bot]'
git config --global user.email '41898282+github-actions[bot]@users.noreply.github.com'
git remote set-url origin "https://x-access-token:$GITHUB_TOKEN@github.com/gmitch215/benchmarks"
git fetch origin master

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
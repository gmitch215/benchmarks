module NumFormat
  def numformat(input)
    return '' if input.nil? || input.to_s.strip.empty?
    input = input.to_s

    if input.match?(/^\d+$/)
      input.chars.reverse.each_slice(3).map(&:join).join(',').reverse
    else
      input
    end
  end
end

Liquid::Template.register_filter(NumFormat)
puts "Number filter loaded"